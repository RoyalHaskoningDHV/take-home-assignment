import {useAlert} from "../providers/AlertProvider";

import './Alert.scss'

type AlertType = 'INFO' | 'ERROR'

export type AlertProps = {
    type: AlertType
    title: string
    message: string
    buttons?: {label: string, onClick: () => void}[]
}

export default function Alert({type, title, message, buttons}: AlertProps) {
    const {dismissAlert} = useAlert()

    return (<div className="alert-backdrop">
        <div className="alert">
            <h3>{title}</h3>
            <p>{message}</p>
            <div className="alert-buttonlist">
                <button onClick={dismissAlert}>Close</button>
                {buttons?.map((button) => <button onClick={button.onClick}>{button.label}</button>)}
            </div>
        </div>
    </div>)
}