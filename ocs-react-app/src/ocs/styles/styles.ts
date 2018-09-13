import styled, {css} from "styled-components";
import * as React from "react";

export const Textarea = styled.textarea`
    position: relative;
    box-sizing: border-box;
    background-color: rgb(255, 255, 255);
    color: rgb(30, 30, 30);
    top: 12px;
    box-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
    margin: 28px;
    font-family: monospace;
    font-size: 14px;
    overflow: auto;
    height: 300px;
    width: 90%;
`;

interface IButtonProps {
    primary: boolean
}

export const Button = styled.button<IButtonProps & React.HTMLProps<HTMLButtonElement>>`
    border-radius: 3px;
    padding: 0.25em 1em;
    margin: 0 1em;
    background: transparent;
    border: 2px solid;
    width: auto;

  ${props => props.primary && css`
    background: white;
  `}
`;
