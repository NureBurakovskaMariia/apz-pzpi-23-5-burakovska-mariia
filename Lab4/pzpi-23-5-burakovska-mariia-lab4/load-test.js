import http from 'k6/http';
import { check } from 'k6';

export const options = {
  stages: [
    { duration: '30s', target: 200 }, 
  ],
};

export default function () {
  const res = http.get('http://localhost:3000/api/animals'); 
  check(res, { 'status is 200': (r) => r.status === 200 });
}