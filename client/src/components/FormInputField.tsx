import {HTMLProps} from "react";

type Props = {
    label: string,
    onChange: (value: string) => void
    placeholder?: string,
} & Omit<HTMLProps<HTMLInputElement>, 'onChange'>

export default function FormInputField({label, onChange, placeholder = " ", ...props}: Props) {
    return (
        <label>
            <p>{label}</p>
            <input placeholder={placeholder} onChange={(event) => onChange(event.target.value)} {...props} />
        </label>
    )
}