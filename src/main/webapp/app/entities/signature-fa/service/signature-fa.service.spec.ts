import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISignatureFa, SignatureFa } from '../signature-fa.model';

import { SignatureFaService } from './signature-fa.service';

describe('Service Tests', () => {
  describe('SignatureFa Service', () => {
    let service: SignatureFaService;
    let httpMock: HttpTestingController;
    let elemDefault: ISignatureFa;
    let expectedResult: ISignatureFa | ISignatureFa[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SignatureFaService);
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

      it('should create a SignatureFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new SignatureFa()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a SignatureFa', () => {
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

      it('should partial update a SignatureFa', () => {
        const patchObject = Object.assign(
          {
            signatureImage: 'BBBBBB',
            width: 1,
            userName: 'BBBBBB',
          },
          new SignatureFa()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of SignatureFa', () => {
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

      it('should delete a SignatureFa', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSignatureFaToCollectionIfMissing', () => {
        it('should add a SignatureFa to an empty array', () => {
          const signature: ISignatureFa = { id: 123 };
          expectedResult = service.addSignatureFaToCollectionIfMissing([], signature);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(signature);
        });

        it('should not add a SignatureFa to an array that contains it', () => {
          const signature: ISignatureFa = { id: 123 };
          const signatureCollection: ISignatureFa[] = [
            {
              ...signature,
            },
            { id: 456 },
          ];
          expectedResult = service.addSignatureFaToCollectionIfMissing(signatureCollection, signature);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a SignatureFa to an array that doesn't contain it", () => {
          const signature: ISignatureFa = { id: 123 };
          const signatureCollection: ISignatureFa[] = [{ id: 456 }];
          expectedResult = service.addSignatureFaToCollectionIfMissing(signatureCollection, signature);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(signature);
        });

        it('should add only unique SignatureFa to an array', () => {
          const signatureArray: ISignatureFa[] = [{ id: 123 }, { id: 456 }, { id: 85680 }];
          const signatureCollection: ISignatureFa[] = [{ id: 123 }];
          expectedResult = service.addSignatureFaToCollectionIfMissing(signatureCollection, ...signatureArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const signature: ISignatureFa = { id: 123 };
          const signature2: ISignatureFa = { id: 456 };
          expectedResult = service.addSignatureFaToCollectionIfMissing([], signature, signature2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(signature);
          expect(expectedResult).toContain(signature2);
        });

        it('should accept null and undefined values', () => {
          const signature: ISignatureFa = { id: 123 };
          expectedResult = service.addSignatureFaToCollectionIfMissing([], null, signature, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(signature);
        });

        it('should return initial array if no SignatureFa is added', () => {
          const signatureCollection: ISignatureFa[] = [{ id: 123 }];
          expectedResult = service.addSignatureFaToCollectionIfMissing(signatureCollection, undefined, null);
          expect(expectedResult).toEqual(signatureCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
