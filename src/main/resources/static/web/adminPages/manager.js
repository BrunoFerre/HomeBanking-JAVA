const {createApp} = Vue

const url = `http://localhost:8080/rest/clients`
console.log(url)
createApp({
    data(){
        return{
            clients:[],
            firstName:'',
            lastName:'',
            email:'',
            jsonRest:''

        }
    },
    created(){
        this.loadData()
    },
    methods:{
        loadData(){
            axios.get(url)
            .then(response =>{
                this.clients = response.data._embedded.clients;
                console.log(this.clients);
                this.jsonRest = JSON.stringify(response.data,null,1)
            })
            .catch(error => console.error(error));
        },
        clientVerify(){
            if (this.firstName && this.lastName && this.email) {
                this.addClient();
            } else {
                window.alert('Please fill in all required fields.');
            }
        },
        addClient(){
        let newClient={
            id:this.id,
            firstName:this.firstName,
            lastName: this.lastName,
            email:this.email,
        }
        axios.post(url,newClient)
        .then(response =>{
            this.firstName='',
            this.lastName='',
            this.email='',
            this.loadData()
        })
        .catch(error => console.error(error))
        }
    }
}).mount("#app")