import { InvoiceWorkflowStatus } from 'app/entities/enumerations/invoice-workflow-status.model';
import { WorkflowAction } from 'app/entities/enumerations/workflow-action.model';
import { PostSpeed } from 'app/entities/enumerations/post-speed.model';

export interface IDocumentInvoiceWorkflow {
  id?: number;
  active?: boolean | null;
  status?: InvoiceWorkflowStatus | null;
  overdueDays?: number | null;
  action?: WorkflowAction | null;
  contactEmail?: string | null;
  speed?: PostSpeed | null;
}

export class DocumentInvoiceWorkflow implements IDocumentInvoiceWorkflow {
  constructor(
    public id?: number,
    public active?: boolean | null,
    public status?: InvoiceWorkflowStatus | null,
    public overdueDays?: number | null,
    public action?: WorkflowAction | null,
    public contactEmail?: string | null,
    public speed?: PostSpeed | null
  ) {
    this.active = this.active ?? false;
  }
}

export function getDocumentInvoiceWorkflowIdentifier(documentInvoiceWorkflow: IDocumentInvoiceWorkflow): number | undefined {
  return documentInvoiceWorkflow.id;
}
