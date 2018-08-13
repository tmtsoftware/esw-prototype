import Client from "./Client";

class EditorClient extends Client {

    constructor(baseUrl: string) {
        super(baseUrl)
    }

    public pause(url:string, callback: (res: string) => void) {
        this.post(`${this.baseUrl}${url}editor/pause`, callback)
    };

    public resume(url:string, callback: (res: string) => void) {
        this.post(`${this.baseUrl}${url}editor/resume`, callback)
    };

    public showSequence(url: string, callback: (res: string) => void) {
        this.post(`${this.baseUrl}${url}editor/sequence`, callback)
    }
}

export default EditorClient;
