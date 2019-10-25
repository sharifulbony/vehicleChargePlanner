<template>
    <div id="app">
        <img src="./assets/logo.png">
        <div class="ui container">
            <my-vuetable></my-vuetable>
        </div>
    </div>
</template>

<script>
    import ticketTable from '../components/ticketTable'

    export default {
        name: 'app',
        components: {
            ticketTable
        }
    }
</script>

<style>
    #app {
        font-family: 'Avenir', Helvetica, Arial, sans-serif;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
        text-align: center;
        color: #2c3e50;
        margin-top: 60px;
    }
</style>