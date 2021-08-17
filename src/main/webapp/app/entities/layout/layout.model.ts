export interface ILayout {
  id?: number;
  remoteId?: number | null;
}

export class Layout implements ILayout {
  constructor(public id?: number, public remoteId?: number | null) {}
}

export function getLayoutIdentifier(layout: ILayout): number | undefined {
  return layout.id;
}
