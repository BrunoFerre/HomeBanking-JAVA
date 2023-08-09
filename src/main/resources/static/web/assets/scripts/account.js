const { createApp } = Vue
createApp({
    data() {
        return {
            client: [],
            accounts: [],
            account: [],
            id_account: Number,
            transaction:[]
        }
    },
    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            this.client= JSON.parse(localStorage.getItem('client'))??[]
            parameter = location.search
            parameterUrl = new URLSearchParams(parameter)
            this.id_account = parameterUrl.get("id")
            
            axios.get(`http://localhost:8080/api/accounts/${this.id_account}`)
                .then(response => {
                    this.account = response.data
                    for(let transaction of this.account.transactionDTOSet){
                        this.transaction.push(transaction)
                    }
                    this.transaction.sort((a,b)=> a.id - b.id)
                    console.log(this.transaction)
                })
        }
    }
}).mount("#app")