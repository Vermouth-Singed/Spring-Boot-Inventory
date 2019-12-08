import Vue from 'vue'
import './plugins'
import './components'
import App from './App.vue'
import VueRouter from 'vue-router'
import 'babel-polyfill'
import 'es6-promise'

Vue.config.productionTip = false;
Vue.use(VueRouter);

new Vue({
    render: h => h(App),
}).$mount('#app');