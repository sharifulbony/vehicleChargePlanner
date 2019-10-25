Vue.use(Vuetable);
new Vue({
    el: '#app',
    components: {
        Multiselect: window.VueMultiselect.default
    },
    data: {

    },
    watch: {


    },
    methods: {

        getAllDeveloper() {
            axios.get('<%=request.getContextPath()%>/getalldeveloper')
                .then(result => {
                document.getElementById("json").innerHTML = JSON.stringify(result.data, undefined, 2);



        })
        .catch(function (error) {
            });
        },


        //    for crud -
        initialize() {

            axios.get('/getinterview')
                .then((response) => {
                this.desserts = response.data;
        })
        .catch(function (error) {
                console.log(error);
            })
                .then(function () {

                });

        },

        editItem(item) {
            this.editedIndex = this.desserts.indexOf(item);
            this.editedItem = Object.assign({}, item);
            this.dialog = true
        },

        deleteItem(item) {

            const index = this.desserts.indexOf(item);
            if (confirm('Are you sure you want to delete this item?')) {
                // this.desserts.splice(index, 1);

                axios.get('/deleteinterview' + item.id)
                    .then((response) => {
                    this.initialize();
                // this.searchAll();
            })
            .catch(function (error) {
                    // handle error
                    console.log(error);
                })
                    .then(function () {
                        // always executed
                    });


            }
        },

        close() {
            this.dialog = false;
            setTimeout(() => {
                this.editedItem = Object.assign({}, this.defaultItem);
                this.editedIndex = -1
            }, 300)
        },

        save() {
            if (this.editedIndex > -1) {
                axios.post('/updateinterview', {
                    "id": this.editedItem.id,
                    "name": this.editedItem.name,
                })
                    .then(result => {
                    this.initialize();

            })
            .catch(function (error) {
                });


            } else {
                console.log('!! inter view add called!!');
                var data = {
                    "name": this.editedItem,
                };
                console.log(JSON.stringify(data));

                axios.post('/createinterview', {"name": this.editedItem.name})
                    .then(result => {
                    this.initialize();

            })
            .catch(function (error) {
                });
                this.desserts.push(this.editedItem)
            }
            this.close()
        },

        getAllData() {
            axios.get('/searchall')
                .then((response) => {
                this.tableItems = response.data;
            document.getElementById('app').style.visibility = 'visible';
        })
        .catch(function (error) {

                console.log(error);
            })
                .then(function () {
                });
        },

    },
    updated: function () {
        this.$nextTick(function () {
        })
    },
    computed: {
        formTitle() {
            return this.editedIndex === -1 ? 'New Item' : 'Edit Item'
        }
    },
    created() {
        this.initialize()
    },


    mounted: function () {
        axios.get('/searchall')
            .then((response) => {
            console.log(response);
        this.tableItems = response.data;
        document.getElementById('app').style.visibility = 'visible';

        this.getAllDeveloper();
    })
    .catch(function (error) {

            console.log(error);
        })
            .then(function () {
            });


    }
});
