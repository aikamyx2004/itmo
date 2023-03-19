<template>
  <aside>
    <SidebarPost v-for="post in posts" :post="post" :user="findUser(post)" :key="post.id"/>
  </aside>
</template>

<script>
import SidebarPost from "./SidebarPost";

export default {
  name: "Sidebar",
  components: {
    SidebarPost
  },
  props: ["posts", "users"],
  methods: {
    findUser(post) {
      return Object.values(this.users).filter(u => u.id === post.id)[0];
    }
  }
}
</script>

<style scoped>

</style>