import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

export const getInstrumentTypeOne = (params) => {
    return getRequest('/instrumentType/getOne', params)
}
export const getInstrumentTypeList = (params) => {
    return getRequest('/instrumentType/getByPage', params)
}
export const getInstrumentTypeCount = (params) => {
    return getRequest('/instrumentType/count', params)
}
export const addInstrumentType = (params) => {
    return postRequest('/instrumentType/insert', params)
}
export const editInstrumentType = (params) => {
    return postRequest('/instrumentType/update', params)
}
export const addOrEditInstrumentType = (params) => {
    return postRequest('/instrumentType/insertOrUpdate', params)
}
export const deleteInstrumentType = (params) => {
    return postRequest('/instrumentType/delByIds', params)
}