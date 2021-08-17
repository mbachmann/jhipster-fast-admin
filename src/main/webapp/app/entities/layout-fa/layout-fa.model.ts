export interface ILayoutFa {
  id?: number;
  remoteId?: number | null;
}

export class LayoutFa implements ILayoutFa {
  constructor(public id?: number, public remoteId?: number | null) {}
}

export function getLayoutFaIdentifier(layout: ILayoutFa): number | undefined {
  return layout.id;
}
