const { createApp } = Vue
import { logout } from './logout.js'
createApp({
    data() {
        return {
            client: [],
            account: [],
            id_account: Number,
            transaction:[],
            error:'',
            nuevoArray:[],
            dateAsherter:[],
            dateInit:'',
            dateEnd:'',
            numberAcc:'',
            showForm: false
        }
    },
    created() {
        this.loadData()
        this.getData()
    },
    methods: {
        loadData() {
            const parameter = location.search
            const parameterUrl = new URLSearchParams(parameter)
            this.id_account = parameterUrl.get("id")
            axios.get(`/api/clients/current/accounts/${this.id_account}`)
                .then(response => {
                    this.account = response.data
                    this.account.balance=this.account.balance.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,')
                    if(this.account.status == false){
                        this.account=[]
                    }      
                    for(let transaction of this.account.transactions){
                        let newObj = {
                            type: transaction.type,
                            amount: transaction.amount.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,'),
                            date: transaction.date,
                            balance: transaction.balance.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,'),
                            description: transaction.description
                        }
                        this.transaction.push(newObj)
                    }
                    this.dateAsherter=this.transaction.map(transaction=>transaction.date.slice(2,-3).replace(/-/g, '/'))
                    this.transaction.sort((a,b)=> a.id - b.id)
            }).catch(error => {
                this.error= error.message
                // location.href = "../pages/error.html"
            })
        },
        getData(){
            axios.get(`/api/clients/current`)
                .then(response => {
                    this.client = response.data
            
                })
        },
        logOut(){
            logout()
        },
        deleteAccount(){ {
            Swal.fire({
                title: 'Delete this account?',
                inputAttributes: {
                    autocapitalize: 'off',
                },
                showCancelButton: true,
                confirmButtonText: 'Yes',
                showLoaderOnConfirm: true,
                preConfirm: login => {
                    return axios
                        .put(`/api/clients/current/accounts/${this.id_account}`)
                        .then(response => {
                            setTimeout(() =>{
                            location.href = "../pages/accounts.html";  
                            },200)
                        }).catch(error => {
                            Swal.fire({
                                icon: 'error',
                                title: 'Oops...',
                                text: error.response.data,
                                confirmButtonColor: '#5b31be93',
                            })
                })
                }
            })
        }
    },
    searchTransactions() {
        const url = `/api/transactions/findDate?dateInit=${this.dateInit}:00&dateEnd=${this.dateEnd}:00&numberAcc=${this.account.number}`;
        axios
            .get(url, { responseType: 'blob' })
            .then((response) => {
                const blob = new Blob([response.data], { type: 'application/pdf' });
                const url = window.URL.createObjectURL(blob);
                const a = document.createElement('a');
                a.href = url;
                a.download = 'transactions-Table.pdf';
                a.click();
                window.URL.revokeObjectURL(url);
            }).catch(error =>{
                console.log(error.response.data.text()
                .then(res=>{
                    console.log(res);
                }));
            }
            );
    },
    showOption(){
        this.showForm=!this.showForm
    }

}
}).mount("#app")