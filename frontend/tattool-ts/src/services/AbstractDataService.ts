import useAxios from "./useAxios";
const { axiosInstance } = useAxios();

abstract class AxiosDataService<T> {

    private noun: string;

    constructor(noun: string) {
        this.noun = noun;
    }

    public async getAll(): Promise<Array<T>> {
        return axiosInstance.get(`/api/v1/${this.noun}s`)
            .then((response) => response.data, (err) => { throw Error(err) });
    }

    public async getOne(id: string): Promise<T> {
        return axiosInstance.get(`/api/v1/${this.noun}s/${id}`)
            .then((response) => response.data, (err) => { throw Error(err) });
    }

    public async saveOne(one: T): Promise<T> {
        return axiosInstance.post(`/api/v1/${this.noun}s`, one)
            .then((response) => response.data, (err) => { throw Error(err) });
    }

    public async deleteOne(id: string): Promise<void> {
        return axiosInstance.delete(`/api/v1/${this.noun}s/${id}`)
            .then((response) => response.data, (err) => { throw Error(err) });
    }


}




export default AxiosDataService;
