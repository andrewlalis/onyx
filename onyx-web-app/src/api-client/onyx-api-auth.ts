import type {OnyxApi} from "@/api-client/onyx-api";

export interface LoginInfo {
  username: string;
  password: string;
}

export interface TokenData {
  token: string;
  expiresAt: number;
}

interface TokenPair {
  accessToken: TokenData;
  refreshToken: TokenData;
}

/**
 * Authentication endpoints in the Onyx API.
 */
export class OnyxAuthApi {
  constructor(readonly api: OnyxApi) {}

  async login(loginInfo: LoginInfo): Promise<TokenPair> {
    const data: any = await this.api.request({method: "POST", url: "/auth/login", data: loginInfo}, true)
    return {
      accessToken: {token: data.accessToken, expiresAt: data.accessTokenExpiresAt},
      refreshToken: {token: data.refreshToken, expiresAt: data.refreshTokenExpiresAt}
    }
  }

  async getAccessToken(refreshToken: string): Promise<TokenData> {
    const data: any = await this.api.request({url: "/auth/access", headers: {"Authorization": "Bearer " + refreshToken}});
    return {
      token: data.accessToken,
      expiresAt: data.expiresAt
    };
  }

  async getTokenExpiration(token: string): Promise<number> {
    const data: any = await this.api.request({url: "/auth/token-expiration", headers: {"Authorization": "Bearer " + token}}, true);
    return data.expiresAt;
  }

  async removeAllRefreshTokens(): Promise<void> {
    await this.api.request({method: "DELETE", url: "/auth/refresh-tokens"})
  }
}