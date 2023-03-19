<template>
  <div>
    <Post v-for="post in sorted(posts)" :post="post" :user="findUser(post, users)"  :key="post.id"/>
  </div>
</template>

<script>
import Post from "@/components/main/Post.vue";

export default {
  name: "Index",
  props: ["posts", "users"],
  components: {
    Post
  },
  methods: {
    sorted: function (array) {
      return Object.values(array).sort((a, b) => b.id - a.id)
    },
    findUser: function (post, users){
      return Object.values(users).filter(u => u.id === post.user.id)[0];
    },
  }
}
</script>

<style scoped>

</style>
