<template>
    <footer>
        <a href="/">Codeforces</a> &copy; 2022 by Mike Mirzayanov
        <div class="count">{{postCount}} posts, {{userCount}} users</div>
    </footer>
</template>

<script>
export default {
    name: "Footer",
    props: ["postCount", "userCount"],
}
</script>

<style scoped>

</style>