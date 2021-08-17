import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISignature, Signature } from '../signature.model';

import { SignatureService } from './signature.service';

describe('Service Tests', () => {
  describe('Signature Service', () => {
    let service: SignatureService;
    let httpMock: HttpTestingController;
    let elemDefault: ISignature;
    let expectedResult: ISignature | ISignature[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SignatureService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        remoteId: 0,
        signatureImageContentType: 'image/png',
        signatureImage: 'AAAAAAA',
        width: 0,
        heigth: 0,
        userName: 'AAAAAAA',
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

      it('should create a Signature', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Signature()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Signature', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
            signatureImage: 'BBBBBB',
            width: 1,
            heigth: 1,
            userName: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Signature', () => {
        const patchObject = Object.assign(
          {
            signatureImage: 'BBBBBB',
            width: 1,
            userName: 'BBBBBB',
          },
          new Signature()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Signature', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
            signatureImage: 'BBBBBB',
            width: 1,
            heigth: 1,
            userName: 'BBBBBB',
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

      it('should delete a Signature', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSignatureToCollectionIfMissing', () => {
        it('should add a Signature to an empty array', () => {
          const signature: ISignature = { id: 123 };
          expectedResult = service.addSignatureToCollectionIfMissing([], signature);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(signature);
        });

        it('should not add a Signature to an array that contains it', () => {
          const signature: ISignature = { id: 123 };
          const signatureCollection: ISignature[] = [
            {
              ...signature,
            },
            { id: 456 },
          ];
          expectedResult = service.addSignatureToCollectionIfMissing(signatureCollection, signature);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Signature to an array that doesn't contain it", () => {
          const signature: ISignature = { id: 123 };
          const signatureCollection: ISignature[] = [{ id: 456 }];
          expectedResult = service.addSignatureToCollectionIfMissing(signatureCollection, signature);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(signature);
        });

        it('should add only unique Signature to an array', () => {
          const signatureArray: ISignature[] = [{ id: 123 }, { id: 456 }, { id: 85680 }];
          const signatureCollection: ISignature[] = [{ id: 123 }];
          expectedResult = service.addSignatureToCollectionIfMissing(signatureCollection, ...signatureArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const signature: ISignature = { id: 123 };
          const signature2: ISignature = { id: 456 };
          expectedResult = service.addSignatureToCollectionIfMissing([], signature, signature2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(signature);
          expect(expectedResult).toContain(signature2);
        });

        it('should accept null and undefined values', () => {
          const signature: ISignature = { id: 123 };
          expectedResult = service.addSignatureToCollectionIfMissing([], null, signature, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(signature);
        });

        it('should return initial array if no Signature is added', () => {
          const signatureCollection: ISignature[] = [{ id: 123 }];
          expectedResult = service.addSignatureToCollectionIfMissing(signatureCollection, undefined, null);
          expect(expectedResult).toEqual(signatureCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
