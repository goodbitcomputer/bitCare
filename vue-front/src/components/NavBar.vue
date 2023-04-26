<template>
  <div class="container">
    <div class="nav-box">
      <b-navbar-toggle target="sidebar-backdrop">
          <b-icon icon="list"></b-icon>
      </b-navbar-toggle>
      <p>비트케어BitCare</p>
    </div>
    <router-link to="/">Home</router-link> |
    <router-link to="/nurse">Nurse</router-link> |
    <router-link to="/doctor">Doctor</router-link>
    <div>
      <b-sidebar
          id="sidebar-backdrop"
          title="Bit Care"
          backdrop
          shadow

      >
        <div class="px-3 py-2">
          <p><router-link to="/">Home</router-link></p>
          <p><router-link to="/nurse">Nurse</router-link></p>
          <p><router-link to="/doctor">Doctor</router-link></p>
          <p><router-link to="/">환자 정보</router-link></p>
          <p><router-link to="/nurse">의료진 정보</router-link></p>
          <p><router-link to="/doctor">쪽지</router-link></p>
        </div>
      </b-sidebar>
    </div>
    <hr>
  </div>

</template>

<script>
export default {
  name: "NavBar"
}
</script>

<style lang="scss" scoped>
  .nav-box {
    display: flex;
  }
</style>