import {Styles} from "jss";
import jss from "jss";

const customstyles: Partial<Styles<any>> = {
    button: {
        background: 'transparent',
        border: '2px solid',
        'border-radius': '3px',
        margin: '0 1em',
        padding: '0.25em 1em',
        width: 'auto'
    },
    textArea: {
        'background-color': 'rgb(255, 255, 255)',
        'box-shadow': '0 1px 2px rgba(0, 0, 0, 0.3)',
        'box-sizing': 'border-box',
        color: 'rgb(30, 30, 30)',
        'font-family': 'monospace',
        'font-size': '14px',
        height: '300px',
        margin: '28px',
        overflow: 'auto',
        position: 'relative',
        top: '12px',
        width: '90%'
    }
};

export const cssClasses = jss.createStyleSheet(customstyles).attach().classes;