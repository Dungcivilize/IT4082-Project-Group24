import axiosClient from './axiosClient';

const apartmentApi = {
    getAll() {
        const url = '/apartments';
        return axiosClient.get(url);
    },

    get(id) {
        const url = `/apartments/${id}`;
        return axiosClient.get(url);
    },

    getCurrentOwnership(id) {
        const url = `/apartments/${id}/current-ownership`;
        return axiosClient.get(url);
    },

    getOwnershipHistory(id) {
        const url = `/apartments/${id}/ownership-history`;
        return axiosClient.get(url);
    },

    assignToUser(id, data) {
        const url = `/apartments/${id}/assign`;
        return axiosClient.post(url, data);
    },

    endOwnership(ownershipId, data) {
        const url = `/apartment-ownerships/${ownershipId}/end`;
        return axiosClient.post(url, data);
    }
}

export default apartmentApi; 