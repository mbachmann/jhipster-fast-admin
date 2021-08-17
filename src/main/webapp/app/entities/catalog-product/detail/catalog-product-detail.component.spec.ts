import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CatalogProductDetailComponent } from './catalog-product-detail.component';

describe('Component Tests', () => {
  describe('CatalogProduct Management Detail Component', () => {
    let comp: CatalogProductDetailComponent;
    let fixture: ComponentFixture<CatalogProductDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CatalogProductDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ catalogProduct: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CatalogProductDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CatalogProductDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load catalogProduct on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.catalogProduct).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
