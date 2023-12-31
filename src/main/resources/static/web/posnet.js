const { createApp } = Vue

createApp({
    data() {
        return {
            cardNumber: "",
            cvv: 0,
            amount: 0,
            description: "",
        }
    },
    created() {

    },
    methods: {
        payLoan() {
            let data ={
                "cardNumber":this.cardNumber,
                "amount": this.amount,
                "cvv": this.cvv,
                "description": this.description
            }
            Swal.fire({
                    title: 'Payment?',
                    inputAttributes: {
                        autocapitalize: 'off',
                    },
                    showCancelButton: true,
                    confirmButtonText: 'Yes',
                    showLoaderOnConfirm: true,
                    preConfirm: login => {
                        return axios.post("/api/payments",data)
                            .then(response => {
                                return Swal.fire({
                                    icon: 'success',
                                    title: 'Success!',
                                    text: response.data,
                                    confirmButtonColor: '#5b31be93',
                                })
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
    }
}).mount('#app')