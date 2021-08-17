import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILayoutFa, LayoutFa } from '../layout-fa.model';

import { LayoutFaService } from './layout-fa.service';

describe('Service Tests', () => {
  describe('LayoutFa Service', () => {
    let service: LayoutFaService;
    let httpMock: HttpTestingController;
    let elemDefault: ILayoutFa;
    let expectedResult: ILayoutFa | ILayoutFa[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(LayoutFaService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        remoteId: 0,
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

      it('should create a LayoutFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new LayoutFa()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a LayoutFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a LayoutFa', () => {
        const patchObject = Object.assign(
          {
            remoteId: 1,
          },
          new LayoutFa()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of LayoutFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
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

      it('should delete a LayoutFa', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addLayoutFaToCollectionIfMissing', () => {
        it('should add a LayoutFa to an empty array', () => {
          const layout: ILayoutFa = { id: 123 };
          expectedResult = service.addLayoutFaToCollectionIfMissing([], layout);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(layout);
        });

        it('should not add a LayoutFa to an array that contains it', () => {
          const layout: ILayoutFa = { id: 123 };
          const layoutCollection: ILayoutFa[] = [
            {
              ...layout,
            },
            { id: 456 },
          ];
          expectedResult = service.addLayoutFaToCollectionIfMissing(layoutCollection, layout);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a LayoutFa to an array that doesn't contain it", () => {
          const layout: ILayoutFa = { id: 123 };
          const layoutCollection: ILayoutFa[] = [{ id: 456 }];
          expectedResult = service.addLayoutFaToCollectionIfMissing(layoutCollection, layout);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(layout);
        });

        it('should add only unique LayoutFa to an array', () => {
          const layoutArray: ILayoutFa[] = [{ id: 123 }, { id: 456 }, { id: 28667 }];
          const layoutCollection: ILayoutFa[] = [{ id: 123 }];
          expectedResult = service.addLayoutFaToCollectionIfMissing(layoutCollection, ...layoutArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const layout: ILayoutFa = { id: 123 };
          const layout2: ILayoutFa = { id: 456 };
          expectedResult = service.addLayoutFaToCollectionIfMissing([], layout, layout2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(layout);
          expect(expectedResult).toContain(layout2);
        });

        it('should accept null and undefined values', () => {
          const layout: ILayoutFa = { id: 123 };
          expectedResult = service.addLayoutFaToCollectionIfMissing([], null, layout, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(layout);
        });

        it('should return initial array if no LayoutFa is added', () => {
          const layoutCollection: ILayoutFa[] = [{ id: 123 }];
          expectedResult = service.addLayoutFaToCollectionIfMissing(layoutCollection, undefined, null);
          expect(expectedResult).toEqual(layoutCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
