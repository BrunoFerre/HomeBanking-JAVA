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
        this.getData()
    },
    methods: {
        newLoans(event) {
            console.log(this.selectLoan);
            event.preventDefault()
            let newLoan = {
                "id": this.selectLoan.id,
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
                    console.log(newLoan);
                return axios.post('/api/loans', newLoan)
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
            axios.get(`/api/clients/current/accounts`)
                .then(response => {
                    const accounts = response.data
                    this.accounts = accounts.filter(account => account.status == true)
                    axios.get(`/api/loans`)
                        .then(response => {
                            this.loans = response.data
                            console.log(this.loans);
                            this.selectLoan.maxAmount=0
                        }).catch(error => {
                            console.log(error);
                        })
                }).catch(error => {
                    console.log(error);
                })
            },
            getData(){
                axios.get(`/api/clients/current`)
                    .then(response => {
                        this.client = response.data
                        console.log(this.clients);
                    })
            },
        logOut() {
            logout()
        }
    },
    computed: {
        calculateInterest() {
            if (this.payment == 6) {
                this.finalAmount = this.amount + (this.amount * (0.10 + (this.selectLoan.interest / 100)))
                return this.finalAmount;
            }
            else if (this.payment == 12) {
                this.finalAmount = this.amount + (this.amount * (0.20 + (this.selectLoan.interest / 100)))
                return this.finalAmount;
            }
            else if (this.payment == 24) {
                this.finalAmount = this.amount + (this.amount * (0.047 + (this.selectLoan.interest / 100)))
                return this.finalAmount;
            }
            else if (this.payment == 36) {
                this.finalAmount = this.amount + (this.amount * (0.65 + (this.selectLoan.interest / 100)))
                return this.finalAmount;
            }
            else if (this.payment == 48) {
                this.finalAmount = this.amount + (this.amount * (0.70 + (this.selectLoan.interest / 100)))
                return this.finalAmount;
            }
            else if (this.payment == 60) {
                this.finalAmount = this.amount + (this.amount * (0.75 + (this.selectLoan.interest / 100)))
                return this.finalAmount;
            } else { return 0 };
        },
    }
}).mount('#app')