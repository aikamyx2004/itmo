<template>
  <section>
    <div class="header">
      {{ post.title }}
    </div>
    <div class="body">
      {{ post.text }}
    </div>
    <div class="footer">
      <a href="#" @click.prevent="openPost">View all</a>
    </div>
  </section>
</template>

<script>

export default {
  name: "SidebarPost",
  props: ["post", "user"],
  methods: {
    openPost: function () {
      this.temporary = this.temporary || {};
      this.temporary.post=this.post;
      this.temporary.user=this.user;
      this.$root.$emit("onChangePage", "Post");
    }
  }
}
</script>

<style scoped>
section {
  margin-bottom: 1rem;
}
</style>