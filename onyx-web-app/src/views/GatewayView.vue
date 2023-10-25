<!--
This is a view shown to unauthenticated users before they login or register
to the node. Attempting to access a non-public piece of content or other page
will redirect to the gateway before the user is allowed to view/edit it.
-->
<script setup lang="ts">

</script>

<template>
  <main>
    <h1>Gateway View</h1>

    <RouterView/>
  </main>
</template>

<style scoped>

</style>