const { createApp } = Vue
import { logout } from './logout.js'
const app = createApp({
    data() {
        return {
            client: [],
            accounts: [],
            loans: [],
            payment: [],
            selectLoan: {},
            paymentFilter:[],
            amount: 0,
            accountDest:''
        }
    },
    created() {
        this.loadData()
    },
    methods: {
        newLoans() {
            let newLoan = {
                "id":this.selectLoan,
                "amount": Number(this.amount),
                "payments": this.payment,
                "accountDestiny": this.accountDest
            }
            console.log(newLoan);
            axios.post('http://localhost:8080/api/loans', newLoan)
                .then(response => {
                    window.reload()
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

                        })
                })
        },
        payments() {
                this.paymentFilter = this.loans.filter(loan => {
                return this.selectLoan == (loan.id)  
            })[0]
        },
        logOut() {
            logout()
        }
    }
}).mount('#app')