const { createApp } = Vue
import { logout } from './logout.js'
createApp({
    data() {
        return {
            client: [],
            account: [],
            id_account: Number,
            transaction:[],
            error:''
        }
    },
    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            this.client= JSON.parse(localStorage.getItem('client'))??[]
           const parameter = location.search
           const parameterUrl = new URLSearchParams(parameter)
            this.id_account = parameterUrl.get("id")
            axios.get(`http://localhost:8080/api/clients/accounts/${this.id_account}`)
                .then(response => {
                    this.account = response.data
                    for(let transaction of this.account.transactionDTOSet){
                        this.transaction.push(transaction)
                    }
                    this.transaction.sort((a,b)=> a.id - b.id)
                    console.log(this.transaction)
            }).catch(error => {
                this.error= error.message
                location.href = "../pages/error.html"
            })
        },
        logOut(){
            logout()
        }
    }
}).mount("#app")