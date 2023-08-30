const {createApp}= Vue
const app = createApp({
    data(){
        return{
            firstName:'',
            lastName:'',
            email:'',
            password:'',
            client:[]
        }
    },
    created(){
        this.loadData()
    },
    methods:{
        loadData(){
            this.client=JSON.parse(localStorage.getItem('client'))
        }
    }
}).mount("#app")