import http from 'k6/http';
import { check, sleep } from 'k6';
import { Counter } from 'k6/metrics';

export let failedRequests = new Counter('failed_requests');
export let options = {
    stages: [
        { duration: '1m', target: 10 },
        { duration: '4m', target: 10 },
        { duration: '1m', target: 0 },
    ],
    thresholds: {
        'failed_requests': ['rate<0.01'],
        'http_req_duration': ['p(95)<300'],
    },
};
export default function () {
    const tenantId = 123;
    const mondayId = Math.floor(Math.random() * 100000) + 1;
    const url = `http://host.docker.internal:8080/users/${tenantId}/${mondayId}`;
    const headers = {
        'Content-Type': 'application/json',
        'X-Timeout': '3500',
    };

    let res = http.get(url, { headers: headers });
    let isSuccess = check(res, {
        'status is 200': (r) => r.status === 200,
        'duration was <= 300ms': (r) => r.timings.duration <= 300,
    });

    if (!isSuccess) {
        failedRequests.add(1);
    }

    sleep(1);
}
