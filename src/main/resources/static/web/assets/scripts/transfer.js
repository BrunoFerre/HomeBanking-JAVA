const { createApp } = Vue
import { logout } from './logout.js'

const app = createApp({
    data() {
        return {
            firstName: '',
            client: [],
            accounts: [],
            origin: '',
            destination: '',
            amount: '',
            description: '',
            showForm1: true,
            showForm2: false  
        }
    },
    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            this.client = JSON.parse(localStorage.getItem('client'))
            axios.get(`/api/clients/current/accounts`)
                .then(response => {
                    this.accounts = response.data

                })
        },
        logOut() {
            logout()
        },
        newTransfer() {
            Swal.fire({
                title: 'Add a new transfer?',
                inputAttributes: {
                    autocapitalize: 'off',
                },
                showCancelButton: true,
                confirmButtonText: 'Yes',
                showLoaderOnConfirm: true,
                buttonColor: '#32a852',
                preConfirm: login => {
                    console.log(this.amount, this.description, this.origin, this.destination);
                    return axios
                        .post('/api/transactions',
                        `amount=${this.amount}&description=${this.description}&accountOrigin=${this.origin}&accountDestination=${this.destination}`,)
                        .then(response => {
                            setTimeout(() => {
                                location.reload()
                            }, 2000)
                        })
                        .catch(error => {
                            console.log(error);
                            Swal.fire({
                                icon: 'error',
                                title: 'Oops...'+ error.response.data,
                            })
                        })
                }
            })
        },
        form1(){
            this.showForm1 = false
            this.showForm2 = true
        },
        form2(){
            this.showForm2 = false
            this.showForm1 = true
        }
    }
}).mount("#app")