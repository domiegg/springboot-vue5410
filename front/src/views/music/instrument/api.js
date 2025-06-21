import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

export const getInstrumentOne = (params) => {
    return getRequest('/instrument/getOne', params)
}
export const getInstrumentList = (params) => {
    return getRequest('/instrument/getByPage', params)
}
export const getInstrumentCount = (params) => {
    return getRequest('/instrument/count', params)
}
export const addInstrument = (params) => {
    return postRequest('/instrument/insert', params)
}
export const editInstrument = (params) => {
    return postRequest('/instrument/update', params)
}
export const addOrEditInstrument = (params) => {
    return postRequest('/instrument/insertOrUpdate', params)
}
export const deleteInstrument = (params) => {
    return postRequest('/instrument/delByIds', params)
}
export const getInstrumentTypeList = (params) => {
    return getRequest('/instrumentType/getAll', params)
}
export const addOrderOnline = (params) => {
    return getRequest('/orderOnline/addOne', params)
}
export const addOrderOffline = (params) => {
    return getRequest('/orderOffline/addOne', params)
}