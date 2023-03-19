<template>
  <div class="middle">
    <Sidebar :posts="viewPosts" :users="users"/>
    <main>
      <Index v-if="page === 'Index'" :posts="posts" :users="users" :comments="comments"/>
      <Enter v-if="page === 'Enter'"/>
      <Register v-if="page === 'Register'"/>
      <WritePost v-if="page === 'WritePost'"/>
      <EditPost v-if="page === 'EditPost'"/>
      <Users v-if="page === 'Users'" :users="users"/>
      <Post v-if="page === 'Post'" :post="takePost" :user="takeUser"/>
    </main>
  </div>
</template>

<script>
import Sidebar from "./sidebar/Sidebar";
import Index from "./page/Index";
import Enter from "./page/Enter";
import WritePost from "./page/WritePost";
import EditPost from "./page/EditPost";
import Register from "./page/Register.vue";
import Users from "@/components/page/Users.vue";
import Post from "@/components/page/Post.vue";

export default {
  name: "Middle",
  data: function () {
    return {
      page: "Index"
    }
  },
  components: {
    Users,
    Register,
    WritePost,
    Enter,
    Index,
    Sidebar,
    EditPost,
    Post
  },
  props: ["posts", "users", "comments"],
  computed: {
    viewPosts: function () {
      return Object.values(this.posts).sort((a, b) => b.id - a.id).slice(0, 2);
    }
  }, beforeCreate() {
    this.$root.$on("onChangePage", (page) => {
      this.page = page
    })
  },
  methods: {
    takePost: function (){
      const post = this["temporary"].post;
      delete this["temporary"].post;
      return post;
    },
    takeUser: function (){
      const user = this["temporary"].user;
      delete this["temporary"].user;
      return user;
    }
  }
}
</script>

<style scoped>

</style>
