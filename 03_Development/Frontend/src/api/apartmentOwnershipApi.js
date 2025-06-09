import axiosClient from './axiosClient';

const apartmentOwnershipApi = {
    getAll() {
        const url = '/apartment-ownerships';
        return axiosClient.get(url);
    },

    get(id) {
        const url = `/apartment-ownerships/${id}`;
        return axiosClient.get(url);
    },

    getByApartment(apartmentId) {
        const url = `/apartment-ownerships/by-apartment/${apartmentId}`;
        return axiosClient.get(url);
    },

    getByUser(userId) {
        const url = `/apartment-ownerships/by-user/${userId}`;
        return axiosClient.get(url);
    },

    create(data) {
        const url = '/apartment-ownerships';
        return axiosClient.post(url, data);
    },

    update(id, data) {
        const url = `/apartment-ownerships/${id}`;
        return axiosClient.put(url, data);
    },

    end(id, data) {
        const url = `/apartment-ownerships/${id}/end`;
        return axiosClient.post(url, data);
    }
}

export default apartmentOwnershipApi; 