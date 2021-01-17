import {AlertProps} from "../components/Alert";
import {createContext, PropsWithChildren, useContext, useState} from "react";

interface AlertHook {
    alertProps?: AlertProps
    showAlert(props: AlertProps): void
    dismissAlert(): void
}

const fakeAlertHook: AlertHook = {
    alertProps: undefined,
    showAlert() {
        throw new Error("This is the fake implementation")
    },
    dismissAlert() {
        throw new Error("This is the fake implementation")
    }
}

const AlertContext = createContext(fakeAlertHook)

export function useAlert() {
    return useContext(AlertContext)
}

export function AlertProvider({children}: PropsWithChildren<{}>) {
    const [alertProps, setAlertProps] = useState<AlertProps>()

    const alertHook: AlertHook = {
        alertProps: alertProps,
        showAlert(props) {
            setAlertProps(props)
        },
        dismissAlert() {
            setAlertProps(undefined)
        }
    }

    return <AlertContext.Provider value={alertHook}>{children}</AlertContext.Provider>
}