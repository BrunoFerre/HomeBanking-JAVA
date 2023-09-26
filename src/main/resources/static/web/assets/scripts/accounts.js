const { createApp } = Vue
import { logout } from './logout.js'
createApp({
    data() {
        return {
            clients: [],
            accounts: [],
            loans: [],
            client: [],
            show: false,
            type: '',
        }
    },
    created() {
        this.loadData()
        this.getData()
    },
    methods: {
        loadData() {
            axios.get(`/api/clients/current`)
                .then(response => {
                    this.clients = response.data
                    axios.get(`/api/clients/current/accounts`)
                        .then(response => {
                            const accounts = response.data
                            for (const account of accounts) {
                                let objet = {
                                    id: account.id,
                                    number: account.number.toLocaleString(),
                                    balance: account.balance,
                                    creationDate: account.creationDate,
                                    type: account.type
                                }
                                this.accounts.push(objet)
                                this.accounts.sort((a, b) => a.id - b.id)
                            }
                            // this.accounts = accounts.filter(account => account.status == true);
                            
                            axios.get(`/api/clients/current/loans`)
                                .then(response => {
                                    this.loans = response.data
                                })
                        })
                })
        },
        getData() {
            axios.get(`/api/clients/current`)
                .then(response => {
                    this.client = response.data
                })
        },
        showOption() {
            this.show = !this.show
        },
        logOut() {
            logout()
        },
        createAccount() {
            Swal.fire({
                title: 'Add a new account?',
                inputAttributes: {
                    autocapitalize: 'off',
                },
                showCancelButton: true,
                confirmButtonText: 'Yes',
                showLoaderOnConfirm: true,
                buttonColor: '#32a852',
                preConfirm: login => {
                    return axios
                        .post('/api/clients/current/accounts', `type=${this.type}`, { headers: { 'accept': 'application/xml' } })
                        .then(response => {
                            setTimeout(() => {
                                location.reload()
                            }, 2000)
                        })
                        .catch(error => {
                            Swal.fire({
                                icon: 'error',
                                text: error.response.data,
                                confirmButtonColor: '#5b31be93',
                            });
                            setTimeout(() => {
                                location.reload()
                            }, 2000)
                        });
                },
                allowOutsideClick: () => !Swal.isLoading(),
            });
        },
    }
}).mount("#app")