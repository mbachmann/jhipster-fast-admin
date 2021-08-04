jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CustomFieldMySuffixService } from '../service/custom-field-my-suffix.service';

import { CustomFieldMySuffixComponent } from './custom-field-my-suffix.component';

describe('Component Tests', () => {
  describe('CustomFieldMySuffix Management Component', () => {
    let comp: CustomFieldMySuffixComponent;
    let fixture: ComponentFixture<CustomFieldMySuffixComponent>;
    let service: CustomFieldMySuffixService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CustomFieldMySuffixComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { snapshot: { queryParams: {} } },
          },
        ],
      })
        .overrideTemplate(CustomFieldMySuffixComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CustomFieldMySuffixComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(CustomFieldMySuffixService);

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
      expect(comp.customFields?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
