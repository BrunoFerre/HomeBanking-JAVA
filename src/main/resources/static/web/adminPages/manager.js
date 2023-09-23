const { createApp } = Vue

const url = `http://localhost:8080/rest/clients`
createApp({
    data() {
        return {
            clients: [],
            firstName: '',
            lastName: '',
            email: '',
            jsonRest: '',
            loanName: '',
            maxAmount: '',
            payment: [],
            interest: '',

        }
    },
    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            axios.get(url)
                .then(response => {
                    this.clients = response.data._embedded.clients;
                    this.jsonRest = JSON.stringify(response.data, null, 1)
                })
                .catch(error => console.error(error));
        },
        clientVerify() {
            if (this.firstName && this.lastName && this.email) {
                this.addClient();
            } else {
                window.alert('Please fill in all required fields.');
            }
        },
        addClient() {
            let newClient = {
                id: this.id,
                firstName: this.firstName,
                lastName: this.lastName,
                email: this.email,
            }
            axios.post(url, newClient)
                .then(response => {
                    this.firstName = '',
                        this.lastName = '',
                        this.email = '',
                        this.loadData()
                })
                .catch(error => console.error(error))
        },
        createLoan() {
            let data = {
                "name": this.loanName,
                "maxAmount": this.maxAmount,
                "payments": this.payment,
                "interest": this.interest
            };
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
                    console.log(data);
                    return axios.post('/api/loans/create', data)
                        .then(response => {
                            Swal.fire({
                                icon: 'success',
                                title: 'Loan created successfully',
                                text: response.data,
                                confirmButtonColor: '#5b31be93',
                            })
                            location.reload()
                        })
                        .catch(error => {
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
        }
    }
}).mount("#app")