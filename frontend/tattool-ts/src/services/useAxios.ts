import axios, { AxiosRequestConfig } from "axios";
import useUIState from "@/state/useUIState";

const { flagNetworkError } = useUIState();

const axiosConfig: AxiosRequestConfig = {
  baseURL: process.env.VUE_APP_API_BASE_URL,
  responseType: "json",
  headers: {
    "Content-Type": "application/json",
  },
};

console.log(
  "process.env.VUE_APP_API_BASE_URL = " + process.env.VUE_APP_API_BASE_URL
);

const axiosInstance = axios.create(axiosConfig);

axiosInstance.interceptors.request.use(
  function (config) {
    // Do something before request is sent
    console.log("before request");
    return config;
  },
  function (error) {
    // Do something with request error
    flagNetworkError();
    console.log("error happened / request " + error);
    return Promise.reject(error);
  }
);

axiosInstance.interceptors.response.use(
  function (config) {
    // Do something before request is sent
    return config;
  },
  function (error) {
    // Do something with request error
    flagNetworkError();
    console.log("error happened / response " + error);
    return Promise.reject(error);
  }
);

export default function useAxios() {
  return { axiosInstance, axiosConfig };
}
