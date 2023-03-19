<template>
  <div>
    <Post v-for="post in sorted(posts)" :post="post" :user="findUser(post, users)" :comments="findComments(post, comments)" :key="post.id"/>
  </div>
</template>

<script>
import Post from "@/components/page/Post.vue";

export default {
  name: "Index",
  props: ["posts", "users", "comments"],
  components: {
    Post
  },
  methods: {
    sorted: function (array) {
      return Object.values(array).sort((a, b) => b.id - a.id)
    },
    findUser: function (post, users){
      return Object.values(users).filter(u => u.id === post.userId)[0];
    },
    findComments: function (post, comments){
      return Object.values(comments).filter(p => p.postId === post.id);
    },
  }
}
</script>

<style scoped>

</style>
