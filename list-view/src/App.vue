<template>
  <v-app>
    <v-toolbar app>
      <v-toolbar-title class="headline text-uppercase">
        <img src="../public/char.gif" style="width:30px;vertical-align:top">
        <span> Assignment</span>
        <span class="font-weight-light">
          <v-layout row child-flex wrap>
            <router-link to="/home"><v-icon>mdi-home</v-icon>Home</router-link>
            &emsp;&emsp;
            <router-link to="/inventory"><v-icon>mdi-format-list-bulleted-square</v-icon>Inventory List</router-link>
          </v-layout>
        </span>
      </v-toolbar-title>
    </v-toolbar>

    <v-content>
      <router-view></router-view>
    </v-content>
  </v-app>
</template>

<style lang="scss">
@import '@/styles/index.scss';

/* Remove in 1.2 */
.v-datatable thead th.column.sortable i {
  vertical-align: unset;
}
</style>

<script>
import VueRouter from 'vue-router'
import HelloWorld from './components/HelloWorld'
import Inventory from './components/Inventory'

const router = new VueRouter({
  routes : [
    {path:"/", component:Inventory},
    {path:"/home", component:HelloWorld},
    {path:"/inventory", component:Inventory}
  ]
});

export default {
  name: 'App',
  data () {
    return {
      //
    }
  },
  router
}
</script>
