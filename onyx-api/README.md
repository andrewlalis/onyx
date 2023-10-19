# onyx-api

The backend API that powers a single onyx node.

This API is built as a modular monolithic web application using the Spring Boot framework, and serves as the access point for a single onyx node. An onyx node is responsible for maintaining a hierarchical collection of notes in various formats (markdown, text, image, etc.), as well as maintain connections to other known nodes for the purpose of sharing access to some or all of the content of the connected nodes.

## System Overview

Each onyx node contains at its core a hierarchical organization of all the documents contained directly within the node. Each tree-node, whether a parent or leaf node, defines some access properties that determine who can access that element and all beneath it. Access rules are designed like so:
- **Public** tree-nodes provide access to the document(s) within to anyone (even unauthenticated, anonymous users from anywhere).
- **Network** tree-nodes provide access to any authenticated user from this node or any other connected node. Essentially, it's public to all users within a connected network.
- **Node** tree-nodes provide access to any authenticated user from only this node. Connected nodes do not have access.
- **Private** tree-nodes provide access to only the user who created the parent node.
- **User** tree-nodes provide access only to one or more users whose onyx UUID is listed. These users may be from any onyx node, even those that don't currently share a connection with the one that owns the user-restricted node.

Each tree-node in an onyx node also contains a detailed history of all modifications that have been made to that tree-node. When users edit a document, they make a series of modifications and then save the result, optionally providing a comment for clarification.

Therefore, you can think of onyx as a more robust, secure digital drive for your notes and documents, which can safely connect to others' drives.

## Authentication

Because each onyx node operates independently, there is no central authentication service, but instead each node maintains a set of users, and issues short-lived tokens granting users access to secure resources. When a user needs to access resources from a networked onyx node, their home node will send the user's id and the home node's own thing.
