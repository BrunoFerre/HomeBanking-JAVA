const { createApp } = Vue
import { logout } from './logout.js'
const app = createApp({
    data() {
        return {
            client: [],
            accounts: [],
            loans: [],
            payment: "",
            selectLoan: '',
            paymentFilter:[],
            amount: "",
            accountDest:'',
        }
    },
    created() {
        this.loadData()
    },
    methods: {
        newLoans(event) {
            event.preventDefault()
            let newLoan = {
                "id":this.selectLoan.id,
                "amount": Number(this.amount),
                "payments": this.payment,
                "accountDestiny": this.accountDest
            }
            console.log(newLoan);
            Swal.fire({
                title: 'Add a new loan?',
                inputAttributes: {
                    autocapitalize: 'off',
                },
                showCancelButton: true,
                confirmButtonText: 'Yes',
                showLoaderOnConfirm: true,
                buttonColor: '#32a852',
                preConfirm: login => {
                return axios.post('http://localhost:8080/api/loans', newLoan)
                    .then(response => {
                        window.location.reload()
                    }).catch(error => {
                        console.log(error);
                        Swal.fire({
                            icon: 'error',
                            text: error.response.data,
                            confirmButtonColor: '#5b31be93',
                        })
                    })
                },
                allowOutsideClick: () => !Swal.isLoading(),
            })
        },
        loadData() {
            this.client = JSON.parse(localStorage.getItem('client')) ?? []
            axios.get(`http://localhost:8080/api/clients/current/accounts`)
                .then(response => {
                    this.accounts = response.data
                    axios.get(`http://localhost:8080/api/loans`)
                        .then(response => {
                            this.loans = response.data
                            console.log(this.loans);
                            this.amount.toLocaleString(1)
                        }).catch(error => {
                            console.log(error);
                        })
                }).catch(error => {
                    console.log(error);
                })
        },
        payments() {
                this.paymentFilter = this.loans.filter(loan => {
                return this.selectLoan.id == (loan.id)  
            })[0]
        },
        logOut() {
            logout()
        }
    }
}).mount('#app')