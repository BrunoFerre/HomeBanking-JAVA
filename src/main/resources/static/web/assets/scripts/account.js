const { createApp } = Vue

createApp({
    data() {
        return {
            clients: [],
            clients_accounts: [],
            accounts:[]
        }
    },
    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            axios.get(`http://localhost:8080/api/clients/`)
                .then(response => {
                    this.clients = response.data
                    this.clients_accounts = this.clients[0].accounts

                    for (const account of this.clients_accounts) {
                       const aux={
                            number : account.number,
                            creationDate: account.creationDate,
                            balance : account.balance
                        }
                        this.accounts.push(aux)
                    }
                })
        }
    }
}).mount("#app")