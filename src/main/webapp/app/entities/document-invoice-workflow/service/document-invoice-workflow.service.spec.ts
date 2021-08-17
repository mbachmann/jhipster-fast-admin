import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { InvoiceWorkflowStatus } from 'app/entities/enumerations/invoice-workflow-status.model';
import { WorkflowAction } from 'app/entities/enumerations/workflow-action.model';
import { PostSpeed } from 'app/entities/enumerations/post-speed.model';
import { IDocumentInvoiceWorkflow, DocumentInvoiceWorkflow } from '../document-invoice-workflow.model';

import { DocumentInvoiceWorkflowService } from './document-invoice-workflow.service';

describe('Service Tests', () => {
  describe('DocumentInvoiceWorkflow Service', () => {
    let service: DocumentInvoiceWorkflowService;
    let httpMock: HttpTestingController;
    let elemDefault: IDocumentInvoiceWorkflow;
    let expectedResult: IDocumentInvoiceWorkflow | IDocumentInvoiceWorkflow[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DocumentInvoiceWorkflowService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        active: false,
        status: InvoiceWorkflowStatus.PAYMENT_REMINDER,
        overdueDays: 0,
        action: WorkflowAction.REMIND_ME,
        contactEmail: 'AAAAAAA',
        speed: PostSpeed.PRIORIRY,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a DocumentInvoiceWorkflow', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new DocumentInvoiceWorkflow()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a DocumentInvoiceWorkflow', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            active: true,
            status: 'BBBBBB',
            overdueDays: 1,
            action: 'BBBBBB',
            contactEmail: 'BBBBBB',
            speed: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a DocumentInvoiceWorkflow', () => {
        const patchObject = Object.assign(
          {
            active: true,
            status: 'BBBBBB',
            overdueDays: 1,
          },
          new DocumentInvoiceWorkflow()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of DocumentInvoiceWorkflow', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            active: true,
            status: 'BBBBBB',
            overdueDays: 1,
            action: 'BBBBBB',
            contactEmail: 'BBBBBB',
            speed: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a DocumentInvoiceWorkflow', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDocumentInvoiceWorkflowToCollectionIfMissing', () => {
        it('should add a DocumentInvoiceWorkflow to an empty array', () => {
          const documentInvoiceWorkflow: IDocumentInvoiceWorkflow = { id: 123 };
          expectedResult = service.addDocumentInvoiceWorkflowToCollectionIfMissing([], documentInvoiceWorkflow);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(documentInvoiceWorkflow);
        });

        it('should not add a DocumentInvoiceWorkflow to an array that contains it', () => {
          const documentInvoiceWorkflow: IDocumentInvoiceWorkflow = { id: 123 };
          const documentInvoiceWorkflowCollection: IDocumentInvoiceWorkflow[] = [
            {
              ...documentInvoiceWorkflow,
            },
            { id: 456 },
          ];
          expectedResult = service.addDocumentInvoiceWorkflowToCollectionIfMissing(
            documentInvoiceWorkflowCollection,
            documentInvoiceWorkflow
          );
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a DocumentInvoiceWorkflow to an array that doesn't contain it", () => {
          const documentInvoiceWorkflow: IDocumentInvoiceWorkflow = { id: 123 };
          const documentInvoiceWorkflowCollection: IDocumentInvoiceWorkflow[] = [{ id: 456 }];
          expectedResult = service.addDocumentInvoiceWorkflowToCollectionIfMissing(
            documentInvoiceWorkflowCollection,
            documentInvoiceWorkflow
          );
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(documentInvoiceWorkflow);
        });

        it('should add only unique DocumentInvoiceWorkflow to an array', () => {
          const documentInvoiceWorkflowArray: IDocumentInvoiceWorkflow[] = [{ id: 123 }, { id: 456 }, { id: 3216 }];
          const documentInvoiceWorkflowCollection: IDocumentInvoiceWorkflow[] = [{ id: 123 }];
          expectedResult = service.addDocumentInvoiceWorkflowToCollectionIfMissing(
            documentInvoiceWorkflowCollection,
            ...documentInvoiceWorkflowArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const documentInvoiceWorkflow: IDocumentInvoiceWorkflow = { id: 123 };
          const documentInvoiceWorkflow2: IDocumentInvoiceWorkflow = { id: 456 };
          expectedResult = service.addDocumentInvoiceWorkflowToCollectionIfMissing([], documentInvoiceWorkflow, documentInvoiceWorkflow2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(documentInvoiceWorkflow);
          expect(expectedResult).toContain(documentInvoiceWorkflow2);
        });

        it('should accept null and undefined values', () => {
          const documentInvoiceWorkflow: IDocumentInvoiceWorkflow = { id: 123 };
          expectedResult = service.addDocumentInvoiceWorkflowToCollectionIfMissing([], null, documentInvoiceWorkflow, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(documentInvoiceWorkflow);
        });

        it('should return initial array if no DocumentInvoiceWorkflow is added', () => {
          const documentInvoiceWorkflowCollection: IDocumentInvoiceWorkflow[] = [{ id: 123 }];
          expectedResult = service.addDocumentInvoiceWorkflowToCollectionIfMissing(documentInvoiceWorkflowCollection, undefined, null);
          expect(expectedResult).toEqual(documentInvoiceWorkflowCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
