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
            axios.get(`http://localhost:8080/api/clients/1`)
                .then(response => {
                    this.client = response.data
                    parameter = location.search
                    parameterUrl = new URLSearchParams(parameter)
                    this.id_account = parameterUrl.get("id")
                    for (const account of this.client.accounts) {
                        this.accounts.push(account)
                    }
                    this.account = this.accounts.find(account => account.id == this.id_account)
                    for(let transaction of this.account.transactionDTOSet){
                        this.transaction.push(transaction)
                    }
                    this.transaction.sort((a,b)=> a.id - b.id)
                    console.log(this.transaction)
                }).catch(error => console.error(error))
        }
    }
}).mount("#app")