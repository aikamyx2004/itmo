<template>
  <div id="app">
    <Header :user="user"/>
    <Middle :posts="posts" :users="users"/>
    <Footer/>
  </div>
</template>

<script>
import Header from "./components/Header";
import Middle from "./components/Middle";
import Footer from "./components/Footer";
import axios from "axios"

export default {
  name: 'App',
  components: {
    Footer,
    Middle,
    Header
  },
  data: function () {
    return {
      user: null,
      posts: [],
      users: []
    }
  },
  beforeMount() {
    if (localStorage.getItem("jwt") && !this.user) {
      this.$root.$emit("onJwt", localStorage.getItem("jwt"));
    }

    axios.get("/api/1/posts").then(response => {
      this.posts = response.data;
    });
    axios.get("/api/1/users").then(response => {
      this.users = response.data;
    });
  },
  beforeCreate() {
    this.$root.$on("onRegister", (name, login, password) => {
      if (!name || name.trim() === "") {
        this.$root.$emit("onRegisterValidationError", "Name is empty");
        return;
      }

      name = name.trim();
      if (name.length > 100) {
        this.$root.$emit("onRegisterValidationError", "Name length can't be more than 100");
        return;
      }

      if (!login || login.trim() === "") {
        this.$root.$emit("onRegisterValidationError", "Login is empty");
        return;
      }
      login = login.trim();
      if (login.length < 2 || login.length > 24) {
        this.$root.$emit("onRegisterValidationError", "Login length can be from 2 to 24");
        return;
      }
      if (login.match("[a-zA-z]+")) {
        this.$root.$emit("onRegisterValidationError", "Login can be only from Latin letters");
        return;
      }

      if (!password || password.trim() === "") {
        this.$root.$emit("onRegisterValidationError", "Password is empty");
        return;
      }
      axios.post("/api/1/users", {name, login, password})
          .then(this.$root.$emit("onChangePage", "Enter"))
          .catch(error => this.$root.$emit("onRegisterValidationError", error.response.data))
    });
    this.$root.$on("onEnter", (login, password) => {
      if (!login || login.trim() === "") {
        this.$root.$emit("onEnterValidationError", "Login is empty");
        return;
      }
      login = login.trim();
      if (login.length < 2 || login.length > 24) {
        this.$root.$emit("onEnterValidationError", "Login length can be from 2 to 24");
        return;
      }

      if (!password || password.trim() === "") {
        this.$root.$emit("onEnterValidationError", "Password is empty");
        return;
      }

      axios.post("/api/1/jwt", {
        login, password
      }).then(response => {
        localStorage.setItem("jwt", response.data);
        this.$root.$emit("onJwt", response.data);
      }).catch(error => {
        this.$root.$emit("onEnterValidationError", error.response.data);
      });
    });

    this.$root.$on("onJwt", (jwt) => {
      localStorage.setItem("jwt", jwt);

      axios.get("/api/1/users/auth", {
        params: {
          jwt
        }
      }).then(response => {
        this.user = response.data;
        this.$root.$emit("onChangePage", "Index");
      }).catch(() => this.$root.$emit("onLogout"))
    });

    this.$root.$on("onLogout", () => {
      localStorage.removeItem("jwt");
      this.user = null;
    });
    this.$root.$on("onWritePost", (title, text) => {
      if (this.user) {
        if (!title) {
          this.$root.$emit("onWritePostValidationError", "Title is empty");
        } else if (!text) {
          this.$root.$emit("onWritePostValidationError", "Text is empty");
        } else {
          let user = this.user;
          console.log(user);
          axios.post("/api/1/posts", {title, text, user})
              .then(this.$root.$emit("onChangePage", "Index"))
              .catch(error => this.$root.$emit("onWritePostValidationError", error.response.data))
        }
      } else {
        this.$root.$emit("onWritePostValidationError", "No access");
      }
    });
  }
}
</script>

<style>
#app {

}
</style>
