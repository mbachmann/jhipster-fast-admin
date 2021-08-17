import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CatalogCategoryService } from '../service/catalog-category.service';

import { CatalogCategoryComponent } from './catalog-category.component';

describe('Component Tests', () => {
  describe('CatalogCategory Management Component', () => {
    let comp: CatalogCategoryComponent;
    let fixture: ComponentFixture<CatalogCategoryComponent>;
    let service: CatalogCategoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CatalogCategoryComponent],
      })
        .overrideTemplate(CatalogCategoryComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CatalogCategoryComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(CatalogCategoryService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.catalogCategories?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
