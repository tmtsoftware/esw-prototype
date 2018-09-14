import Client from "./Client";

class EditorClient extends Client {

    constructor(resourcePath: string) {
        super(resourcePath)
    }

    public pause(callback: (res: string) => void) {
        this.post(`${this.resourcePath}editor/pause`, callback)
    };

    public resume(callback: (res: string) => void) {
        this.post(`${this.resourcePath}editor/resume`, callback)
    };

    public showSequence(callback: (res: string) => void) {
        this.get(`${this.resourcePath}editor/sequence`, callback)
    }
}

export default EditorClient;
