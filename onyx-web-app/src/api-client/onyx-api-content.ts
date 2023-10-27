import type {OnyxApi} from "@/api-client/onyx-api";

export interface ContentNode {
  id: number;
  name: string;
  nodeType: string;
  archived: boolean;
}

export interface ContainerChildData {
  id: number;
  name: string;
  nodeType: string;
}

export interface ContainerNode extends ContentNode {
  children: ContainerChildData[]
}

export interface DocumentNode extends ContentNode {
  contentType: string;
}

export interface ContainerNodeCreationData {
  name: string;
}

export interface DocumentNodeCreationData {
  name: string;
  contentType: string;
  content: string;
}

/**
 * The API for interacting with nodes in an Onyx instance's content tree.
 */
export class OnyxContentApi {
  constructor(readonly api: OnyxApi) {}

  async getNode(id: number): Promise<ContainerNode | DocumentNode> {
    return await this.api.request({url: "/content/nodes/" + id});
  }

  async getRoot(): Promise<ContainerNode> {
    return await this.api.request({url: "/content/nodes/root"});
  }

  async createContainer(parentNodeId: number, data: ContainerNodeCreationData): Promise<ContainerNode> {
    return await this.api.request({
      method: "POST",
      url: "/content/nodes/" + parentNodeId + "/children",
      data: data
    });
  }

  async createDocument(parentNodeId: number, data: DocumentNodeCreationData): Promise<DocumentNode> {
    return await this.api.request({
      method: "POST",
      url: "/content/nodes/" + parentNodeId + "/children",
      data: data
    });
  }

  async deleteNode(id: number): Promise<void> {
    await this.api.request({method: "DELETE", url: "/content/nodes/" + id});
  }
}