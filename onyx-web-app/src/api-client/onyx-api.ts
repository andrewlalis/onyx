import type {AxiosRequestConfig} from "axios";
import axios, {Axios} from "axios";
import {OnyxAuthApi} from "@/api-client/onyx-api-auth";
import type{LoginInfo, TokenData} from "@/api-client/onyx-api-auth";
import {OnyxContentApi} from "@/api-client/onyx-api-content";

interface OnyxAuthState {
  accessToken: TokenData | null;
  refreshToken: TokenData | null;
}

export class OnyxApi {
  private authState: OnyxAuthState = {accessToken: null, refreshToken: null};
  private accessTokenIntervalId: number | null = null;
  private httpClient: Axios;

  readonly auth: OnyxAuthApi = new OnyxAuthApi(this);
  readonly content: OnyxContentApi = new OnyxContentApi(this);

  constructor() {
    this.httpClient = axios.create({
      baseURL: "http://localhost:8080/",
      timeout: 3000
    });
  }

  /**
   * Loads a new API instance from information in the user's local storage,
   * which should ideally have a stored refresh token and its expiration date.
   * Using this information, we can fetch a new access token and any other auth
   * data. Will return an API instance that's authenticated if we have the info
   * to do so, and all necessary services are available. Otherwise, an
   * unauthenticated instance is returned.
   */
  static async loadFromLocalStorage(): Promise<OnyxApi> {
    const refreshToken = localStorage.getItem("onyx-api-refresh-token");
    const expirationStr = localStorage.getItem("onyx-api-refresh-token-expiration");
    let expiration = (expirationStr !== null) ? parseInt(expirationStr, 10): null;
    const api = new OnyxApi();
    // If no expiration was stored, try and get that first.
    if (refreshToken && expiration === null) {
      try {
        expiration = await api.auth.getTokenExpiration(refreshToken);
      } catch (error: any) {
        console.error(error);
        return api;
      }
    }
    if (expiration === null) return api; // This is to make the type-checker happy.
    // If we have a valid refresh token that's not expired, get an access token and init the auth state.
    if (refreshToken && expiration > Date.now()) {
      try {
        api.authState.accessToken = await api.auth.getAccessToken(refreshToken);
        api.authState.refreshToken = {token: refreshToken, expiresAt: expiration};
        api.saveAuthStateToLocalStorage();
      } catch (error: any) {
        console.error(error);
      }
    }
    return api;
  }

  private saveAuthStateToLocalStorage() {
    if (this.authState.refreshToken) {
      localStorage.setItem("onyx-api-refresh-token", this.authState.refreshToken?.token);
      localStorage.setItem("onyx-api-refresh-token-expiration", this.authState.refreshToken.expiresAt.toString(10));
    } else {
      localStorage.removeItem("onyx-api-refresh-token");
      localStorage.removeItem("onyx-api-refresh-token-expiration");
    }
    console.log("Saved current auth state to local storage.", this.authState.refreshToken);
  }

  private initAccessTokenRefreshing() {
    if (this.accessTokenIntervalId) {
      window.clearInterval(this.accessTokenIntervalId);
    }
    this.accessTokenIntervalId = window.setInterval(async () => {
      if (this.authState.refreshToken && this.authState.accessToken && this.authState.accessToken.expiresAt < Date.now() + 5000) {
        this.authState.accessToken = await this.auth.getAccessToken(this.authState.refreshToken.token);
        this.saveAuthStateToLocalStorage();
        console.log("Updated access token.");
      }
    }, 5000);
  }

  /**
   * Attempts to do a login with the given credentials to obtain a new refresh token.
   * @param credentials The credentials to use.
   */
  async login(credentials: LoginInfo): Promise<void> {
    const tokens = await this.auth.login(credentials);
    this.authState = {
      accessToken: tokens.accessToken,
      refreshToken: tokens.refreshToken
    };
    this.saveAuthStateToLocalStorage();
  }

  /**
   * Logs this instance out if it was logged in.
   */
  logout() {
    if (this.accessTokenIntervalId) {
      window.clearInterval(this.accessTokenIntervalId);
    }
    this.authState = {accessToken: null, refreshToken: null};
    this.saveAuthStateToLocalStorage();
  }

  /**
   * Makes a request to the Onyx API, setting the Authorization header when
   * this API instance is authenticated.
   * @param config The axios request object.
   * @param anon Whether to make the request anonymous (no auth).
   */
  async request<R>(config: AxiosRequestConfig, anon: boolean = false): Promise<R> {
    if (!config.headers) config.headers = {};
    if (!anon && this.isAuthenticated()) {
      config.headers["Authorization"] = "Bearer " + this.authState.accessToken?.token;
    }
    if (config.data) {
      config.headers["Content-Type"] = "application/json";
    }
    const response = await this.httpClient.request(config);
    if (response.status === 200) {
      return response.data;
    }
    throw response.status;
  }

  /**
   * Tells whether this API instance is authenticated and likely able to
   * access endpoints that require authentication.
   */
  isAuthenticated(): boolean {
    return this.authState.accessToken !== null &&
      this.authState.accessToken.expiresAt > Date.now() &&
      this.authState.refreshToken !== null &&
      this.authState.refreshToken.expiresAt > Date.now();
  }
}
