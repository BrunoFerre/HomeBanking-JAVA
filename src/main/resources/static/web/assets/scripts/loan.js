const { createApp } = Vue
import { logout } from './logout.js'
const app = createApp({
    data() {
        return {
            client: [],
            accounts: [],
            loans: [],
            payment: "",
            selectLoan: [],
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
                "amount": Number(this.amount),
                "payments": this.payment,
                "accountDestiny": this.accountDest,
            }
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
                    const accounts = response.data
                    this.accounts = accounts.filter(account => account.status == true)
                    axios.get(`http://localhost:8080/api/loans`)
                        .then(response => {
                            this.loans = response.data
                        }).catch(error => {
                            console.log(error);
                        })
                }).catch(error => {
                    console.log(error);
                })
            },
        logOut() {
            logout()
        }
    },
    computed: {
        calculateInterest() {
            if (this.payment == 3) {
                this.finalAmount = this.amount + (this.amount * (0.005 + (this.selectLoan.interest / 100)))
                return this.finalAmount;
            }
            else if (this.payment == 6) {
                this.finalAmount = this.amount + (this.amount * (0.010 + (this.selectLoan.interest / 100)))
                return this.finalAmount;
            }
            else if (this.payment == 12) {
                this.finalAmount = this.amount + (this.amount * (0.020 + (this.selectLoan.interest / 100)))
                return this.finalAmount;
            }
            else if (this.payment == 24) {
                this.finalAmount = this.amount + (this.amount * (0.045 + (this.selectLoan.interest / 100)))
                return this.finalAmount;
            }
            else if (this.payment == 36) {
                this.finalAmount = this.amount + (this.amount * (0.065 + (this.selectLoan.interest / 100)))
                return this.finalAmount;
            }
            else if (this.payment == 48) {
                this.finalAmount = this.amount + (this.amount * (0.070 + (this.selectLoan.interest / 100)))
                return this.finalAmount;
            }
            else if (this.payment == 60) {
                this.finalAmount = this.amount + (this.amount * (0.075 + (this.selectLoan.interest / 100)))
                return this.finalAmount;
            } else { return 0 };
        }
    }
}).mount('#app')