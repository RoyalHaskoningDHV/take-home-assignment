import {useState} from "react";
import {SearchCarClient} from "../proto/searchCar_pb_service";

export default function useSearchCarClient() {
    const [searchCarClient] = useState(new SearchCarClient('http://localhost:8080'))
    return searchCarClient
}